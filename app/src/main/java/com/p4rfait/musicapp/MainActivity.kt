package com.p4rfait.musicapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.p4rfait.musicapp.ui.albums.AlbumsFragment
import com.p4rfait.musicapp.ui.songs.SongsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialmente cargamos la pantalla de Álbumes
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment, AlbumsFragment())
            }
        }
    }
}
