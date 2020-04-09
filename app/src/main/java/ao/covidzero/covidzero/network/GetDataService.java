package ao.covidzero.covidzero.network;

import org.json.JSONObject;

import java.util.List;

import ao.covidzero.covidzero.model.Dado;
import ao.covidzero.covidzero.model.Grupo;
import ao.covidzero.covidzero.model.Mensagem;
import ao.covidzero.covidzero.model.Profissional;
import ao.covidzero.covidzero.model.Provincia;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("dados")
    Call<Dado> dadosGerais();

    @GET("dados/{provincia}")
    Call<Dado> dadosProvincia(@Path("provincia") String provincia);

    @GET("insideGrupo/{id}")
    Call<List<Mensagem>> grupoSms(@Path("id") int id);

    @GET("grupos/")
    Call<List<Grupo>> grupos();

    @GET("profissionaisSaude")
    Call<List<Profissional>> profissionais();

    @GET("provincias")
    Call<List<Provincia>> getprovincias();

    @FormUrlEncoded
    @POST("usuarios")
    Call<JSONObject> regisgerUser(@Field("telefone") String telefone,@Field("senha") String senha, @Field("nome") String nome);

    @POST("accaoUser/login")
    Call<JSONObject> makeLogin(@Body LoginBody body);

}