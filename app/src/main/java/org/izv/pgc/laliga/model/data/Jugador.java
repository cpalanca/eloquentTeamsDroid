package org.izv.pgc.laliga.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Jugador implements Parcelable {
    private long id , idequipo;
    private String nombre, apellidos, foto;

    public Jugador() {
        this(0, 0, "", "","");
    }

    public Jugador(long id, long idequipo, String nombre, String apellidos, String foto) {
        this.id = id;
        this.idequipo = idequipo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
    }

    protected Jugador(Parcel in) {
        id = in.readLong();
        idequipo = in.readLong();
        nombre = in.readString();
        apellidos = in.readString();
        foto = in.readString();
    }

    public static final Creator<Jugador> CREATOR = new Creator<Jugador>() {
        @Override
        public Jugador createFromParcel(Parcel in) {
            return new Jugador(in);
        }

        @Override
        public Jugador[] newArray(int size) {
            return new Jugador[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdequipo() {
        return idequipo;
    }

    public void setIdequipo(long idequipo) {
        this.idequipo = idequipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", idequipo=" + idequipo +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(idequipo);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(foto);
    }
}
