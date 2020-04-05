package ao.covidzero.covidzero

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded


class MapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var mapboxMap: MapboxMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(
            this,
            "pk.eyJ1Ijoia2FsdW5ndWx1bmdvIiwiYSI6ImNqemk5NWc4MzEzeTUzcGxpcDdmaDFrbWQifQ.48Jm8F2CztKvsBLzYrU7Pw"
        )
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)


    }

    private fun enableLocationComponent(loadedMapStyle: Style) { // Check if permissions are enabled and if not request
        if (mapboxMap != null && PermissionsManager.areLocationPermissionsGranted(this)) { // Get an instance of the component
            val locationComponent: LocationComponent = mapboxMap?.getLocationComponent()!!
            // Activate with options
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle).build()
            )
            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true
            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING
            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager?.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {
        Toast.makeText(this, "Permitir aceder a sua locação para mostrar resultados na sua região", Toast.LENGTH_LONG)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap?.getStyle(OnStyleLoaded { style -> enableLocationComponent(style) })
        } else {
            Toast.makeText(this, "Localização não configurada.", Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onMapReady(pmapboxMap: MapboxMap) {
        mapboxMap = pmapboxMap;

        mapboxMap?.setStyle(Style.SATELLITE, object : OnStyleLoaded {
            override fun onStyleLoaded(style: Style) {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
                enableLocationComponent(style)
            }
        })
    }

}
