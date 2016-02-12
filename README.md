Welcome to the Android-ComboBox wiki!

Main idea is that bind android "AutoCompleteTextView" to list of custom objects like .NET windows forms Combobox Does

it accepts ArrayList<T> as Source

**How it works**

first you need place view on your layout file like this
```xml
<LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ge.ndc.ndc.controls.ComboBox android:id="@+id/StuffOneCombo"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textAutoComplete|textAutoCorrect"
            android:hint="Some Hint"
            />
</LinearLayout>
```
where "ge.ndc.ndc.controls" is your package name 

then you can attach Data Source Like that , DisplayMember and ValueMember must be public Fields in your object

```java

public class StuffListModel {
    public int ID;
    public String FullName;
}
```

```java

    ArrayList<StuffListModel> DataSource=new ArrayList<StuffListModel>();

    StuffListModel item1=new StuffListModel();
    item1.ID=1;
    item1.FullName="Some Name";

    StuffListModel item2=new StuffListModel();
    item1.ID=2;
    item1.FullName="Some Name two";
    DataSource.add(item1);
    DataSource.add(item2);


    ComboBox combo=(ComboBox)this.findViewById(R.id.StuffOneCombo);

    combo.SetDataSource(DataSource,"FullName", "ID");
```            
AND set selected value 

`combo.SetSelectedValue(1);`

also you can set source like this
```java
combo.SetDisplayMember("FullName");
combo.SetValueMember("ID");
combo.SetDataSource(DataSource);
```

and after user selects item you can get what you need 
```java
//Returns bound object Ex. StuffListModel
StuffListModel ob=(StuffListModel)combo.GetSelectedItem();
//gets selected text
GetSelectedText()
//gets Selected Value 
GetSelectedValue()
```

when user starts type and deletes some text or leaves text field empty selectedItem,SelectedText,SelectedValue becomes null until user clicks item from dropdown

if user clicks to combobox dropdown activates itself

also it changes colors to search matches from items 

![sc1](https://github.com/beqadevnosadze/Android-ComboBox/blob/master/ScreenShots/Screenshot_2016-02-10-22-53-31.png?raw=true)

![sc2](https://github.com/beqadevnosadze/Android-ComboBox/blob/master/ScreenShots/Screenshot_2016-02-10-22-53-53.png?raw=true)

![sc3](https://github.com/beqadevnosadze/Android-ComboBox/blob/master/ScreenShots/Screenshot_2016-02-10-22-54-02.png?raw=true)

