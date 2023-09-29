package com.example.botonad

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private lateinit var lightTextView: TextView
    private lateinit var accelerationTextView: TextView
    private lateinit var toggleButton: Button
    private var areSensorsActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lightTextView = findViewById(R.id.luzTextView)
        accelerationTextView = findViewById(R.id.aceleracionTextView) // Asegúrate de que este ID está correcto
        toggleButton = findViewById(R.id.toggleButton)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        toggleButton.setOnClickListener {
            if (areSensorsActive) unregisterSensors()
            else registerSensors()
            areSensorsActive = !areSensorsActive
        }

        if (savedInstanceState != null) {
            areSensorsActive = savedInstanceState.getBoolean("areSensorsActive")
            if (areSensorsActive) registerSensors()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("areSensorsActive", areSensorsActive)
    }

    private fun registerSensors() {
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun unregisterSensors() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_LIGHT -> {
                    val lightValue = event.values[0]
                    lightTextView.text = "Luz: $lightValue"
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    val accelerationX = event.values[0]
                    val accelerationY = event.values[1]
                    val accelerationZ = event.values[2]
                    accelerationTextView.text = "Aceleración:\nX: $accelerationX\nY: $accelerationY\nZ: $accelerationZ"
                }
            }
        }
    }
}
