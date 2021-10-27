package es.uniovi.eii.sdm.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Pelicula implements Parcelable {
    int id;

    String titulo;
    String argumento;
    Categoria categoria;
    String duracion;
    String fecha;

    String urlCaratula;
    String urlFondo;
    String urlTrailer;

    public Pelicula(){};

    public Pelicula(int id,String titulo, String argumento, Categoria categoria, String duracion, String fecha, String urlCaratula, String urlFondo, String urlTrailer) {
        this.id = id;
        this.titulo = titulo;
        this.argumento = argumento;
        this.categoria = categoria;
        this.duracion = duracion;
        this.fecha = fecha;
        this.urlCaratula = urlCaratula;
        this.urlFondo = urlFondo;
        this.urlTrailer = urlTrailer;
    }

    protected Pelicula(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        argumento = in.readString();
        categoria = in.readParcelable(Categoria.class.getClassLoader());
        duracion = in.readString();
        fecha = in.readString();
        urlCaratula = in.readString();
        urlFondo = in.readString();
        urlTrailer = in.readString();
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(titulo);
        parcel.writeString(argumento);
        parcel.writeParcelable(categoria,flags);
        parcel.writeString(duracion);
        parcel.writeString(fecha);
        parcel.writeString(urlCaratula);
        parcel.writeString(urlFondo);
        parcel.writeString(urlTrailer);
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArgumento() {
        return argumento;
    }

    public void setArgumento(String argumento) {
        this.argumento = argumento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUrlCaratula() { return urlCaratula; }

    public void setUrlCaratula(String urlCaratula) { this.urlCaratula = urlCaratula; }

    public String getUrlFondo() { return urlFondo; }

    public void setUrlFondo(String urlFondo) { this.urlFondo = urlFondo; }

    public String getUrlTrailer() { return urlTrailer; }

    public void setUrlTrailer(String urlTrailer) { this.urlTrailer = urlTrailer; }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id='"+ id +'\'' +
                "titulo='" + titulo + '\'' +
                ", argumento='" + argumento + '\'' +
                ", categoria=" + categoria +
                ", duracion='" + duracion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", urlCaratula='" + urlCaratula + '\'' +
                ", urlFondo='" + urlFondo + '\'' +
                ", urlTrailer='" + urlTrailer + '\'' +
                '}';
    }
}
