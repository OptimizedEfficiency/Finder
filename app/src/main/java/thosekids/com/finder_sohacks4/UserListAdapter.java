package thosekids.com.finder_sohacks4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<UserInformation> {

    public UserListAdapter(Context context, List<UserInformation> items) {
        super(context, R.layout.row_user_list, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_user_list, null);
        }

        UserInformation userInformation = getItem(position);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView username = (TextView) v.findViewById(R.id.username);

        name.setText(userInformation.name);
        username.setText(userInformation.username);

        return v;
    }
}
