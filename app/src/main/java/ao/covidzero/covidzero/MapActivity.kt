package ao.covidzero.covidzero

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.model.Provincia
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
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
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var mapboxMap: MapboxMap? = null
    private var provincias:List<Provincia>? = mutableListOf()


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

        loadProvincias()

    }

    fun showLoading(boolean: Boolean=true){
        if(boolean){
            Alerter.create(this@MapActivity)
                .setTitle("Aguarde por favor")
                .setText("A carregar dados das províncias")
                .setBackgroundColorRes(R.color.orange)
                .enableProgress(true)
                .enableInfiniteDuration(true)
                .show()
        } else {
            Alerter.hide()
        }
    }

    fun showTentar(boolean: Boolean=true){
        if(boolean){
            Alerter.create(this@MapActivity)
                .setTitle("Lamentamos")
                .setText("Não foi possível carregar dados. Clique aqui para tentar de novo")
                .setBackgroundColorRes(R.color.red)
                .enableInfiniteDuration(true)
                .setOnClickListener(
                    View.OnClickListener {
                        loadProvincias()
                    }
                )
                .show()
        } else {
            Alerter.hide()
        }
    }

    fun loadProvincias(){
        showLoading()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )
        val call: Call< List<Provincia> > = service.getprovincias()
        call.enqueue(object : Callback<List<Provincia> > {
            override fun onFailure(call: Call<List<Provincia> >, t: Throwable) {
                t.printStackTrace()

                showLoading(false)
                showTentar()
            }

            override fun onResponse(call: Call<List<Provincia> >, response: Response<List<Provincia>>) {
                provincias = response.body()
                showLoading(false)
                showTentar(false)

                Alerter.create(this@MapActivity)
                    .setTitle("Tudo pronto")
                    .setText("Dados das províncias carregados.")
                    .setDuration(4000)
                    .setBackgroundColorRes(R.color.green)
                    .show()
            }
        })
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
