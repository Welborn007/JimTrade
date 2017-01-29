package jimtrade.com.jimtrade;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jimtrade.com.jimtrade.Supplier_contents.Supplier_Categories_Fragment;
import jimtrade.com.jimtrade.Supplier_contents.Supplier_Contact;
import jimtrade.com.jimtrade.Supplier_contents.Supplier_Product_Fragment;
import jimtrade.com.jimtrade.Supplier_contents.Supplier_profile;

public class SupplierSlidingFragments extends Fragment
{
    private Context context;
    static final String LOG_TAG = "SlidingTabsBasicFragment";
    SamplePagerAdapter adapterViewPager;

    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link android.view.View} which will be displayed by this {@link android.support.v4.app.Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sample, container, false);

    }

    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)} has finished.
     * Here we can pick out the {@link android.view.View}s we need to configure from the content view.
     * <p/>
     * We set the {@link android.support.v4.view.ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link android.support.v4.view.ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapterViewPager = new SamplePagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setOffscreenPageLimit(3);

        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    public class SamplePagerAdapter extends FragmentStatePagerAdapter
    {

        public SamplePagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);

        }

        @Override
        public int getCount() {
            return 3;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Products";
                case 1:
                    return "Categories";
                case 2:
                    return "Contact";
                default:
                    return null;
            }
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {

            switch (position)
            {
                case 0:
                    return Supplier_Product_Fragment.newInstance(1, "Supplier Product");
                case 1:
                    return Supplier_Categories_Fragment.newInstance(2, "Supplier Categories");
                case 2:
                    return Supplier_Contact.newInstance(3, "Supplier Contact");
                default:
                    return null;
            }

        }


    }
}
