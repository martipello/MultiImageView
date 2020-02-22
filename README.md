I forked this library to carry on it's development, I've extended the examples to include a recycler view example, the shape  and corner radius can now be set from xml, 


to create the image view in xml 

    <com.stfalcon.multiimageview.MultiImageView
        android:id="@+id/iv"
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:shape="rectangle"
        app:corner_radius="60" />
        
![Image description](https://github.com/martipello/MultiImageView/blob/master/sample/src/main/res/drawable/screenshot_1582383274.png)

        
![Image description](https://github.com/martipello/MultiImageView/blob/master/sample/src/main/res/drawable/screenshot_1582383283.png)


to add an image use 

   multiImageView.addImage(BitmapFactory.decodeResource(getResources(), R.drawable.avatar1));
                    
to clear images use

   multiImageView.clear();
   
the corner radius only applys to the rectangle shape other wise it will be ignored you can set this in xml or programmatically
        
![Image description](https://github.com/martipello/MultiImageView/blob/master/sample/src/main/res/drawable/screenshot_1582383289.png)

