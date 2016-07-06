package cf.app.com.shannon;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by David on 17/04/2016.
 */
public class ServiceAdapter extends ArrayAdapter<Servicio>{

    public ServiceAdapter(Context context,List<Servicio> servicios) {
        super(context,android.R.layout.simple_list_item_1, servicios);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Take the data source at position (e.g. 0)
        // Get the data item
        Servicio servicio = getItem(position);

        // Check if we are using a recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_servicio, parent, false);
        }

        // Lookup the subview within the template
        ImageView imgService = (ImageView) convertView.findViewById(R.id.imgPhoto);
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        TextView tvCategoria = (TextView) convertView.findViewById(R.id.tvCategoria);

        // Populate the subviews (textfield, imageview) with the correct data
        tvTitulo.setText(servicio.getName());
        tvDescripcion.setText("Descripcion: "+servicio.getDescripcion());
        tvCategoria.setText("Categoria: "+servicio.getCategoria());


        // use device width for photo height
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        imgService.getLayoutParams().height = displayMetrics.widthPixels;

        // Reset the images from the recycled view
        imgService.setImageResource(0);

        // Ask for the photo to be added to the imageview based on the photo url
        // Background: Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap into the imageview
        Picasso.with(getContext()).load(servicio.getUriImageService()).into(imgService);
        // Return the view for that data item
        return convertView;
    }





}
