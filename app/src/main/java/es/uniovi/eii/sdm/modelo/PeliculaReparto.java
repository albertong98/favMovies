package es.uniovi.eii.sdm.modelo;



public class PeliculaReparto {
    int id_actor;
    int id_pelicula;
    String nombre_personaje;

    public PeliculaReparto(){}

    public PeliculaReparto(int id_actor, int id_pelicula, String nombre_personaje) {
        this.id_actor = id_actor;
        this.id_pelicula = id_pelicula;
        this.nombre_personaje = nombre_personaje;
    }

    public int getId_actor() { return id_actor; }

    public void setId_actor(int id_actor) { this.id_actor = id_actor; }

    public int getId_pelicula() { return id_pelicula; }

    public void setId_pelicula(int id_pelicula) { this.id_pelicula = id_pelicula; }

    public String getNombre_personaje() { return nombre_personaje; }

    public void setNombre_personaje(String nombre_personaje) { this.nombre_personaje = nombre_personaje; }

    @Override
    public String toString() {
        return "PeliculaReparto{" +
                "id_actor=" + id_actor +
                ", id_pelicula=" + id_pelicula +
                ", nombre_personaje='" + nombre_personaje + '\'' +
                '}';
    }
}
