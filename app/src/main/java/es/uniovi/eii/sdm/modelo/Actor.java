package es.uniovi.eii.sdm.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Actor implements Parcelable {
    int id;
    String nombre;
    String imagen;
    String URL_imdb;
    String nombre_personaje;

    public Actor(){}

    public Actor(int id, String nombre, String imagen, String URL_imdb) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.URL_imdb = URL_imdb;
    }

    public Actor(String nombre,String nombre_personaje, String imagen, String URL_imdb){
        this.nombre = nombre;
        this.nombre_personaje = nombre_personaje;
        this.imagen = imagen;
        this.URL_imdb = URL_imdb;
    }

    public static final Creator<Pelicula> CREATOR = new Creator<Pelicula>() {
        @Override
        public Pelicula createFromParcel(Parcel in) {
            return new Pelicula(in);
        }

        @Override
        public Pelicula[] newArray(int size) {
            return new Pelicula[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nombre);
        parcel.writeString(imagen);
        parcel.writeString(URL_imdb);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagen() { return imagen; }

    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getURL_imdb() { return URL_imdb; }

    public void setURL_imdb(String URL_imdb) { this.URL_imdb = URL_imdb; }

    public String getNombre_personaje() {
        return nombre_personaje;
    }

    public void setNombre_personaje(String nombre_personaje) {
        this.nombre_personaje = nombre_personaje;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", imagen='" + imagen + '\'' +
                ", URL_imdb='" + URL_imdb + '\'' +
                '}';
    }
}
