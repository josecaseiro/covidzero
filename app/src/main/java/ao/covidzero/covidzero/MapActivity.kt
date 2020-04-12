package ao.covidzero.covidzero

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import ao.covidzero.covidzero.model.Dado
import ao.covidzero.covidzero.model.Provincia
import ao.covidzero.covidzero.network.GetDataService
import ao.covidzero.covidzero.network.RetrofitClientInstance
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_VIEWPORT
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class MapActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener,
    LocationEngineCallback<LocationEngineResult> {

    private  var style: Style? = null
    private var mapView: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var mapboxMap: MapboxMap? = null
    private var provincias: List<Provincia>? = null
    val ICON_ID = "ICON_ID";




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

    fun showLoading(boolean: Boolean = true) {
        if (boolean) {
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

    fun showTentar(boolean: Boolean = true) {
        if (boolean) {
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

    fun loadProvincias() {
        showLoading()
        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )
        val call: Call<List<Provincia>> = service.getprovincias()
        call.enqueue(object : Callback<List<Provincia>> {
            override fun onFailure(call: Call<List<Provincia>>, t: Throwable) {
                t.printStackTrace()

                showLoading(false)
                showTentar()
            }

            override fun onResponse(
                call: Call<List<Provincia>>,
                response: Response<List<Provincia>>
            ) {
                provincias = response.body()
                showLoading(false)
                showTentar(false)

                Alerter.create(this@MapActivity)
                    .setTitle("Tudo pronto")
                    .setText("Dados das províncias carregados.")
                    .setDuration(4000)
                    .setBackgroundColorRes(R.color.green)
                    .show()

                style?.let {
                    loadMarkers(it)
                }
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

            initLocationEngine()

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
        Toast.makeText(
            this,
            "Permitir aceder a sua locação para mostrar resultados na sua região",
            Toast.LENGTH_LONG
        )
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

        val drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.red_marker, null);
        val mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)

        mapboxMap?.setStyle(Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

            // Add the SymbolLayer icon image to the map style
            .withImage(
                ICON_ID, mBitmap!!
            )
            , object : OnStyleLoaded {
                override fun onStyleLoaded(sty: Style) {
                    // Map is set up and the style has loaded. Now you can add data or make other map adjustments.
                    style = sty
                    loadMarkers(style!!)
                    enableLocationComponent(sty)

                }
            })
    }

    private fun showProvinciadata(dado: Dado?){
        if(dado != null) {
            dado.let {
                prov.text = it.provincia?.nome2
                suspeitos.text = "Supeitos: " + it.suspeitos.toString()
                positivos.text = "Positivos: " + it.positivos.toString()
                mortos.text = "Mortos: " +it.mortes.toString()
                negativos.text = "Negativos: " +it.negativos.toString()
                quarentena.text = "Em Quarentena: " +it.quarentena.toString()
            }
        } else {
            prov.clearComposingText()
            suspeitos.clearComposingText()
            mortos.clearComposingText()
            negativos.clearComposingText()
            quarentena.clearComposingText()
        }
    }

    private fun loadProvinciaData(prov:String){
        Alerter.create(this@MapActivity)
            .setTitle("Carregando")
            .setText("A carregar dados da província selecionada.")
            .enableInfiniteDuration(true)
            .setBackgroundColorRes(R.color.orange)
            .show()

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )
        val call: Call<Dado> = service.dadosProvincia(prov)
        call.enqueue(object : Callback<Dado> {
            override fun onFailure(call: Call<Dado>, t: Throwable) {
                t.printStackTrace()
                Alerter.hide()
                Alerter.create(this@MapActivity)
                    .setTitle("Lamentamos")
                    .setText("Não foi possível carregar dados da província selecionada.")
                    .setBackgroundColorRes(R.color.red)
                    .show()
            }

            override fun onResponse(call: Call<Dado>, response: Response<Dado>) {
                Alerter.hide()
                showProvinciadata(response.body())
            }
        })

    }
    private fun loadMarkers(style: Style) {
        val symbolManager = SymbolManager(mapView!!, mapboxMap!!, style)

        symbolManager.addClickListener { symbol ->
            Log.d("Selected", symbol.textField)
            loadProvinciaData(symbol.textField)
        }

        symbolManager.iconAllowOverlap = false
        symbolManager.iconTranslate = arrayOf(-4f, 5f)
        symbolManager.iconRotationAlignment = ICON_ROTATION_ALIGNMENT_VIEWPORT


        provincias?.let {
            for (prov in it) {
                // Add symbol at specified lat/lon
                val symbol = symbolManager.create(
                    SymbolOptions()
                        .withTextField(prov.nome)
                        .withLatLng(LatLng(prov.getLat(), prov.getLong()))
                        .withIconImage(ICON_ID)
                        .withIconSize(2.0f)
                )
            }

            /*
            val position = CameraPosition.Builder()
                .target(LatLng(it.first().getLat(), it.first().getLong())) // Sets the new camera position
                .zoom(7.0) // Sets the zoom
                .tilt(30.0) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder


            mapboxMap?.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position), 7000
            );

             */
        }




    }

    override fun onSuccess(result: LocationEngineResult?) {
        val  location = result?.getLastLocation();

        if(location != null)
            animateCamara(location)
    }

    private fun animateCamara(location: Location) {
        val position = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude)) // Sets the new camera position
            .zoom(9.0) // Sets the zoom
            .tilt(30.0) // Set the camera tilt
            .build(); // Creates a CameraPosition from the builder


        mapboxMap?.animateCamera(
            CameraUpdateFactory
                .newCameraPosition(position), 4000
        );
    }

    override fun onFailure(exception: Exception) {

    }

    fun initLocationEngine(){
        val locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        val request = LocationEngineRequest.Builder(1000)
        .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
        .setMaxWaitTime(10000).build();

        locationEngine.requestLocationUpdates(request, this, getMainLooper());
        locationEngine.getLastLocation(this);
    }


}

