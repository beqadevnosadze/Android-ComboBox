package ge.ndc.ndc.controls;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import java.util.ArrayList;

/**
 * Created by beqa on 10.02.2016.
 */
public class ComboBox extends AutoCompleteTextView{

    private ArrayList<?> _dataSource;
    private Object _selectedValue;
    private Object _selectedItem;
    private String _selectedText;
    private String _displayMember;
    private String _valueMember;


    public ComboBox(Context context, AttributeSet attrs){
        super(context,attrs);
        SetupComboBox();
    }
    public ComboBox(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        SetupComboBox();
    }
    public ComboBox(Context context) {
        super(context);
        SetupComboBox();
    }


    private void SetupComboBox(){
        this.setThreshold(1);
        final AutoCompleteTextView  textView=this;

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.showDropDown();
            }
        });

        //Set Onclick Listener When Item Clicked
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Object item=arg0.getItemAtPosition(arg2);
                if(item != null){
                    Object val= null;
                    try {
                        val = item.getClass().getField(_valueMember).get(item);
                        Object text=item.getClass().getField(_displayMember).get(item);
                        _selectedValue=val;
                        _selectedText=text.toString();
                        _selectedItem=item;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                _selectedValue=null;
                _selectedText="";
                _selectedItem=null;
                return false;
            }
        });



//        this.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    public void SetDisplayMember( String value ){
        _displayMember=value;
}

    public void SetValueMember( String value ){
        _valueMember=value;
    }

    public Object GetSelectedItem(){
        return _selectedItem;
    }

    public Object GetSelectedText(){
        return _selectedText;
    }

    public Object GetSelectedValue(){
        return _selectedValue;
    }

    public void SetSelectedValue(Object value) {
        if(value==null){
            setText("");
            _selectedValue=value;
            _selectedText="";
            _selectedItem=value;
            return;
        }
        if(_dataSource != null && _valueMember != null){
            for (Object v:_dataSource){
                try {
                    Object val=v.getClass().getField(_valueMember).get(v);
                    if(val.equals(value)){
                        Object text=v.getClass().getField(_displayMember).get(v);
                        setText(text.toString());
                        _selectedValue=val;
                        _selectedText=text.toString();
                        _selectedItem=v;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Object GetDataSource(){
        return _dataSource;
    }

    public <T> void SetDataSource(ArrayList<T> source,Activity context,String DisplayMember,String ValueMember){

        _displayMember=DisplayMember;
        _valueMember=ValueMember;

        SetDataSource(source,  context);
    }

   public <T> void SetDataSource(ArrayList<T> source,Activity context){

       _dataSource=(ArrayList<T>)source.clone();

       ComboAdapter comboAdapter=new ComboAdapter(context,android.R.layout.simple_dropdown_item_1line,source,_displayMember);

       setAdapter(comboAdapter);
   }


}
