package fr.lille.bour.armand.waterryday.activity;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.lille.bour.armand.waterryday.R;
import fr.lille.bour.armand.waterryday.models.Plant;
import fr.lille.bour.armand.waterryday.models.database.DatabaseHelper;
import fr.lille.bour.armand.waterryday.models.database.PlantDB;

/**
 * An activity representing a list of Plants. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PlantDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PlantListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private final List<Plant> mPlants = new ArrayList<>();

    private SQLiteOpenHelper mHelper;
    private RecyclerView mRecyclerView;
    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new DatabaseHelper(this);
        PlantDB.getInstance().cleanTable(mHelper);
        PlantDB.getInstance().fillWithValues(mHelper);

        // Load plants from database
        new GetAllPlantsTask().execute();

        setContentView(R.layout.activity_plant_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.plant_list);
        mAdapter = new SimpleItemRecyclerViewAdapter(mPlants);
        mRecyclerView.setAdapter(mAdapter);

        if (findViewById(R.id.plant_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        public SimpleItemRecyclerViewAdapter(List<Plant> items) {
            // Nothing to do here
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.plant_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mPlants.get(position);
            holder.mIdView.setText(mPlants.get(position).getName());
            holder.mContentView.setText(mPlants.get(position).getSpecie());

//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(PlantDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                        PlantDetailFragment fragment = new PlantDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.plant_detail_container, fragment)
//                                .commit();
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, PlantDetailActivity.class);
//                        intent.putExtra(PlantDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                        context.startActivity(intent);
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mPlants.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Plant mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    /**
     * Fetches all the plants in database.
     */
    public class GetAllPlantsTask extends AsyncTask<Void, Void, List<Plant>> {

        @Override
        protected List<Plant> doInBackground(Void... voids) {
            final List<Plant> plants = PlantDB.getInstance().getAll(mHelper);
            return plants;
        }

        @Override
        protected void onPostExecute(List<Plant> plants) {
            super.onPostExecute(plants);
            if (!plants.isEmpty()) {
                for (final Plant plant : plants) {
                    mPlants.add(plant);
                }
                mAdapter.notifyItemInserted(mPlants.size() - 1);
            } else {
                Toast.makeText(PlantListActivity.this, R.string.toast_fetch_failed, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Deletes all the object with the specified id, and returns the number of rows deleted.
     */
    public class DeletePlantTask extends AsyncTask<Plant, Void, Plant> {

        private boolean success;

        @Override
        protected Plant doInBackground(Plant... plants) {
            success = PlantDB.getInstance().delete(mHelper, plants[0].getId());
            return plants[0];
        }

        @Override
        protected void onPostExecute(final Plant plant) {
            super.onPostExecute(plant);
            if (success) {
                final int position = mPlants.indexOf(plant);
                mPlants.remove(plant);
                mAdapter.notifyItemRemoved(position);
            } else {
                Toast.makeText(PlantListActivity.this, R.string.toast_delete_failed, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Updates all specified {@link Plant} objects, and returns the number of rows updated.
     */
    public class UpdatePlantTask extends AsyncTask<Plant, Void, Plant> {

        private boolean success;

        @Override
        protected Plant doInBackground(final Plant... plants) {
            success = PlantDB.getInstance().update(mHelper, plants[0]);
            return plants[0];
        }

        @Override
        protected void onPostExecute(final Plant plant) {
            super.onPostExecute(plant);
            if (success) {
                for (final Plant mPlant : mPlants) {
                    if (mPlant.getId() == plant.getId()) {
                        mPlant.setName(plant.getName());
                        mPlant.setSpecie(plant.getSpecie());
                        mPlant.setLocation(plant.getLocation());
                        mPlant.setWateringFrequency(plant.getWateringFrequency());
                        mPlant.setLastWateredDate(plant.getLastWateredDate());
                        break;
                    }
                }
            } else {
                Toast.makeText(PlantListActivity.this, R.string.toast_update_failed, Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * Inserts all specified {@link Plant} objects, and returns the number of rows inserted.
     */
    public class InsertPlantTask extends AsyncTask<Plant, Void, Plant> {

        private boolean success;

        @Override
        protected Plant doInBackground(Plant... plants) {
            success = PlantDB.getInstance().insert(mHelper, plants[0]) != -1;
            return plants[0];
        }

        @Override
        protected void onPostExecute(Plant plant) {
            super.onPostExecute(plant);
            if (success) {
                mPlants.add(plant);
                mAdapter.notifyItemInserted(mPlants.size() - 1);
            } else {
                Toast.makeText(PlantListActivity.this, R.string.toast_insert_failed, Toast.LENGTH_LONG).show();
            }
        }
    }
}
