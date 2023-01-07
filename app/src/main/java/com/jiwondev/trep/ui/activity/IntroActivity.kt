package com.jiwondev.trep.ui.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.databinding.ActivityIntroBinding
import com.jiwondev.trep.resource.App.Companion.coroutineDispatcher
import com.jiwondev.trep.resource.App.Companion.dataStore
import com.jiwondev.trep.resource.PreferencesKeys
import com.jiwondev.trep.ui.viewmodel.AuthViewModel
import com.jiwondev.trep.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.properties.Delegates


class IntroActivity : AppCompatActivity() {
    //    var value = ""
//    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trep_preference")
//
//    private var bool: Boolean by Delegates.notNull()
    lateinit var viewModel: AuthViewModel
    lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        clickListener()




        // 테스트용 액티비티
        /** body에 들어온 비디오 스트리밍 테스트**/


//        val test: Flow<Boolean> = dataStore.data
//            .map { preferences ->
//                bool = preferences[PreferencesKeys.AUTO_LOGIN] ?: true
//                preferences[PreferencesKeys.AUTO_LOGIN] ?: true
//            }
//
//        GlobalScope.launch {
//            val test = AuthRepository(
//                authRemoteDataSource = AuthRemoteDataSource(coroutineDispatcher),
//                authLocalDataSource = AuthLocalDataSource(dataStore)
//            ).getByteVideo()
//
//            val input: InputStream = ByteArrayInputStream(test.byteStream().readBytes())
//        }
//
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.d("value : ", value.toUri().toString())
//
//
//            val test = "https://localhost:3000/b43a577a-cc16-4149-8e84-75238bdc0427"
//            val player = SimpleExoPlayer.Builder(this).build()
//            val mediaItem = MediaItem.fromUri(test)
//           // val mediaItem = MediaItem.fromUri(Uri.parse(test))
//
//            Log.d("test : ", Uri.parse(test).toString())
//
//            player.apply {
//                setMediaItem(mediaItem)
//                prepare()
//                playWhenReady = true
//            }
//
//            val exo = findViewById<PlayerView>(R.id.exo)
//            exo.player = player
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(
                AuthRepository(
                    authRemoteDataSource = AuthRemoteDataSource(coroutineDispatcher),
                    authLocalDataSource = AuthLocalDataSource(dataStore)
                )
            )
        )[AuthViewModel::class.java]
    }

    private fun userLogin() {
        viewModel.getLogin()

        lifecycleScope.launchWhenStarted {
            viewModel.loginFlow.collect {
                when(it) {
                    null -> Toast.makeText(this@IntroActivity, "올바른 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    else -> {
                        // TODO : Datastore write -> login, tokens
                    }
                }
            }
        }
    }

    private fun clickListener() {
        binding.loginButton.setOnClickListener {
            userLogin()
        }
    }
}
