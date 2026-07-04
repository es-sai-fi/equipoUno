package com.example.picobotella.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.picobotella.R
import com.example.picobotella.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)
  }
}
