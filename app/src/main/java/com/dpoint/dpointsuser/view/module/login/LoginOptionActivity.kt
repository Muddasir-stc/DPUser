package com.dpoint.dpointsuser.view.module.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.auth.LoginModel
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoint.dpointsuser.view.module.login.LoginViewModel
import com.dpoints.view.module.dashboard.Dashboard
import com.dpoints.view.module.login.Login
import com.dpoints.view.module.signup.SignUp
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_login_option.*

class LoginOptionActivity : BaseActivity() {
    override val layout: Int
        get() = R.layout.activity_login_option
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    private lateinit var callbackManager: CallbackManager

    override fun init() {
        auth = FirebaseAuth.getInstance()
        setUpGoogleSign()
        setUpFacebookLogin()
        getFirebseToken()
        textView_google.setOnClickListener {
            signInWithGoogle()
        }
        textView_facebook.setOnClickListener {
            signInWithFacebook()
        }
        textView_email.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        textView_signup.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        addObserver()
    }

    private fun setUpGoogleSign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setUpFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
    }
    var token: String = ""
    private fun getFirebseToken(): String {

        FirebaseInstanceId.getInstance()
            .instanceId.addOnSuccessListener(this) { instanceIdResult: InstanceIdResult ->
            token = instanceIdResult.token
            Log.e("newToken", token)
        }
        return token
    }

    private fun signInWithGoogle() {
        if (isNetworkConnected()) {
            auth.signOut()
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, 111)
            }
        }
    }

    private fun signInWithFacebook() {
        val loginManager = LoginManager.getInstance()

        if (AccessToken.getCurrentAccessToken() != null) {
            loginManager.logOut()
        }

        loginManager.logInWithReadPermissions(this, listOf("public_profile", "email"))
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                firebaseAuthWithFacebook(result?.accessToken)
            }

            override fun onCancel() {
                hideProgress()
            }

            override fun onError(error: FacebookException?) {
                hideProgress()
                onFailure(findViewById(R.id.root_view), error?.message ?: "Facebook login failed")
            }
        })

    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed - ", e)
                hideProgress()
                onFailure(findViewById(R.id.root_view), "Google sign-in failed")
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {

        fun signInSuccess(user: FirebaseUser?) {
            val uid = user?.uid ?: return
            val email = user.email ?: return
            val name = user.displayName ?: return
            val imageUri = user.photoUrl
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener
                    val token = task.result?.token ?: return@addOnCompleteListener
                    Log.i("uid", uid)
                    Log.i("email", email)
                    Log.i("name", "Hello")
                    if (BuildConfig.DEBUG) Log.i("Token", token)

                    viewModel.socialLogin("Google", uid, name, email, token)

                    hideProgress()

                }
        }

        fun signInFailure(e: Exception?) {
            hideProgress()
            Log.w("TAG", e ?: Throwable("signInWithGoogle:failure"))
            onFailure(findViewById(R.id.root_view), "Google sign-in failed")
        }

        val credential = GoogleAuthProvider.getCredential(account?.idToken ?: return, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                when {
                    task.isSuccessful -> signInSuccess(auth.currentUser)
                    else -> signInFailure(task.exception)
                }
            }
            .addOnFailureListener { signInFailure(it) }
    }


    internal fun firebaseAuthWithFacebook(accessToken: AccessToken?) {
        accessToken ?: return

        fun loginSuccess(user: FirebaseUser?) {
            val uid = user?.uid ?: return
            val email = user.email
                ?: return onFailure(message = "No email address associated with this facebook account.")
            val name = user.displayName ?: return
            val imageUri = user.photoUrl
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener
                    val token = task.result?.token ?: return@addOnCompleteListener

                    if (BuildConfig.DEBUG) Log.i("Token", token)
                    viewModel.socialLogin("Facebook", uid, name, email, token)
                    hideProgress()
//                    onSocialLoginSuccess()
                }
        }

        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loginSuccess(auth.currentUser)
                } else {
                    hideProgress()
                    onFailure(
                        findViewById(R.id.root_view),
                        task.exception?.message ?: "Facebook login failed"
                    )
                }
            }
    }

    private fun addObserver() {
        viewModel.socialState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    Log.d("chaa", state.data!!.message)
                    onLoginSuccess(state.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun onLoginSuccess(login: LoginModel) {
        Log.e("LOOGGED", "INNN")
        UserPreferences.instance.saveToken(this, login.token)
        UserPreferences.instance.saveUser(this, login.user!!)
        UserPreferences().setLoggedIn(this)
        startActivity<Dashboard>()
        finish()
    }
}
