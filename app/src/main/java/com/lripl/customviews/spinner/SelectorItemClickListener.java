package com.lripl.customviews.spinner;

public interface SelectorItemClickListener {


    void itemClicked(int position);

    /*
    * This method is used to give default selection to display language screen.
    * if we will not provide any selection and click
    * on continue button at Display Language Screen then it will always pick 0th index of List<DisplayDTO>.
    * This method is DisplayLanguage Screen Specific.
    * */
    void defaultSelection(int position);


}
