package com.example.luzhumbral


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private lateinit var lightValueTextView: TextView
    private lateinit var lightMessageTextView: TextView
    private val lightThreshold = 10.0f // Define tu propio umbral de luz.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lightValueTextView = findViewById(R.id.lightValueTextView)
        lightMessageTextView = findViewById(R.id.lightMessageTextView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            lightMessageTextView.text = "Este dispositivo no tiene sensor de luz."
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val lightValue = event.values[0]
                lightValueTextView.text = "Luz: $lightValue lx"

                if (lightValue <= lightThreshold) {
                    lightMessageTextView.text = "Luz insuficiente"
                } else {
                    lightMessageTextView.text = "Luz suficiente"
                }
            }
        }
    }
}
