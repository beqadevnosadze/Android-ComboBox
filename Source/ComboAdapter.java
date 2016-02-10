package ge.ndc.ndc.controls;



import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ge.ndc.ndc.R;

/**
 * Created by beqa on 10.02.2016.
 */
public class ComboAdapter<T> extends ArrayAdapter<T> {

    Context _context;
    int _resource;
    List<T> _items,_tempItems,_suggestions;
    String _filterString="";

    private String _DisplayMember;

    public ComboAdapter(Context context, int resource,ArrayList<T> Items,String DisplayMember) {
        super(context, resource,Items);
        _context=context;
        _resource=resource;
        _items=Items;
        _DisplayMember=DisplayMember;
        _suggestions = new ArrayList<T>();
        _tempItems=new ArrayList<T>(Items);//(ArrayList<T>)Items.clone();
    }

    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 78, 238));
    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_combo, parent, false);
        }
        Object item = _items.get(position);
        if (item != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null){
                String value="";
                try {
                    value=item.getClass().getField(_DisplayMember).get(item).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }


                final SpannableStringBuilder sb = new SpannableStringBuilder(value);
                try {
                    int index=TextUtils.indexOf(value.toLowerCase(),_filterString.toLowerCase() );
                    if(index >= 0 && _filterString!=null && _filterString.length() > 0){
                        int length=_filterString.length();

                        sb.setSpan(fcs,index, index+length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        sb.setSpan(bss,index, index+length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);


                        lblName.setText(sb);
//                    value = value.replaceAll(_filterString,"<font color='#004eee'><b>"+_filterString+"</b></font>");
//                    lblName.setText(Html.fromHtml(value));
                    }
                    else {
                        lblName.setText(value);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


//                value = value.replaceAll(_filterString,"<b>"+_filterString+"</b>");

//                lblName.setText(Html.fromHtml(value));

//
//                sb.clear();
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }



    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String value="";
            try {
                value=resultValue.getClass().getField(_DisplayMember).get(resultValue).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return value;
        }

        boolean FilterStarted=false;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(FilterStarted){
                return new FilterResults();
            }
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                FilterStarted=true;
                _suggestions.clear();

                if(_tempItems.size() == 0){
                    return new FilterResults();
                }


                if (constraint == null || constraint.length() == 0) {
                    ArrayList<T> list;
                    list = new ArrayList<T>(_tempItems);
                    filterResults.values = list;
                    filterResults.count = list.size();
                    return filterResults;
                }

                Field field = null;
                try {
                   field=_tempItems.get(0).getClass().getField(_DisplayMember);
                }  catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    return new FilterResults();
                }

                String prefixString = constraint.toString().toLowerCase();
                final int count = _tempItems.size();
                for (int i = 0; i < count; i++) {
                    final T item = _tempItems.get(i);
                    String valueText="";
                    try {
                        valueText=field.get(item).toString().toLowerCase();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }


                    if (valueText.startsWith(prefixString)) {
                        _suggestions.add(item);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                _suggestions.add(item);
                                break;
                            }
                        }
                    }

                }


                filterResults.values = _suggestions;
                filterResults.count =_suggestions.size();

                FilterStarted=false;
                return filterResults;
            } else {
                filterResults.values = _tempItems;
                filterResults.count =_tempItems.size();
                _filterString="";
                return filterResults;
            }
        }

        private boolean publishStarted=false;
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(publishStarted) {
                return;
            }

            if(constraint != null){
                _filterString=constraint.toString();
            }
            else {
                _filterString="";
            }

            if (results != null && results.count > 0) {

                List<T> filterList = (ArrayList<T>) results.values;
                publishStarted=true;
                clear();
                final int count = filterList.size();
                for (int i = 0; i < count; i++) {
                    if(i>filterList.size()) break;
                    T item=filterList.get(i);
                    add(item);
                }
                notifyDataSetChanged();

                publishStarted=false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    };
}
