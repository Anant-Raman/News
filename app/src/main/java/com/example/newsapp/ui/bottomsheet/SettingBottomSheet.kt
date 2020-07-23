package com.example.newsapp.ui.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.newsapp.R
import com.example.newsapp.core.Constants
import com.example.newsapp.ui.webview.WebViewActivity
import com.example.newsapp.utility.SharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView

class SettingBottomSheet : BottomSheetDialogFragment() {

    private lateinit var countryList: ArrayList<String>
    private lateinit var countryLabelList: ArrayList<String>
    private lateinit var countrySpinner: Spinner
    private lateinit var civGithub: CircleImageView
    private lateinit var civLinkedin: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_setting_bottom_sheet, container, false)
        initSpinner(root)
        initWebView(root)
        return root
    }

    fun initSpinner(view: View) {
        countrySpinner = view.findViewById(R.id.setCountrySpinner)
        countryList = initCountryList()
        countryLabelList = initCountryLabelList()

        val adapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            countryLabelList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter

        countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(position>0) {
                        SharedPreferences.StoreStringSharedPref(
                            Constants.COUNTRY,
                            countryList.get(position),
                            requireContext()
                        )
                    }
                }
            }
    }

    fun initWebView(view: View) {
        civGithub = view.findViewById(R.id.civ_github)
        civLinkedin = view.findViewById(R.id.civ_linkedIn)

        civGithub.setOnClickListener {
            launchWebView(Constants.URL_GITHUB)
        }

        civLinkedin.setOnClickListener {
            launchWebView(Constants.URL_LINKEDIN)
        }

    }

    private fun launchWebView(urlWeb: String) {
        val intent = Intent(requireContext(), WebViewActivity::class.java)
        intent.putExtra(Constants.URL_LABEL, urlWeb)
        this.startActivity(intent)
    }

    private fun initCountryList(): ArrayList<String> {

        val conList = arrayListOf<String>()
        conList.add("")
        conList.add("in")
        conList.add("ar")
        conList.add("at")
        conList.add("ae")
        conList.add("au")
        conList.add("be")
        conList.add("bg")
        conList.add("br")
        conList.add("ca")
        conList.add("ch")
        conList.add("cn")
        conList.add("co")
        conList.add("cu")
        conList.add("cz")
        conList.add("de")
        conList.add("eg")
        conList.add("fr")
        conList.add("gb")
        conList.add("gr")
        conList.add("hk")
        conList.add("hu")
        conList.add("id")
        conList.add("ie")
        conList.add("il")
        conList.add("it")
        conList.add("jp")
        conList.add("kr")
        conList.add("lt")
        conList.add("lv")
        conList.add("ma")
        conList.add("mx")
        conList.add("my")
        conList.add("ng")
        conList.add("nl")
        conList.add("no")
        conList.add("nz")
        conList.add("ph")
        conList.add("pl")
        conList.add("pt")
        conList.add("ro")
        conList.add("rs")
        conList.add("ru")
        conList.add("sa")
        conList.add("se")
        conList.add("sg")
        conList.add("si")
        conList.add("sk")
        conList.add("th")
        conList.add("tr")
        conList.add("tw")
        conList.add("ua")
        conList.add("us")
        conList.add("ve")
        conList.add("za")

        return conList
    }

    private fun initCountryLabelList(): ArrayList<String> {

        val countryLabelList = arrayListOf<String>()
        countryLabelList.add(("Select your country"))
        countryLabelList.add("India")
        countryLabelList.add("Argentina")
        countryLabelList.add("Austria")
        countryLabelList.add("United Arab Emirates")
        countryLabelList.add("Australia")
        countryLabelList.add("Belgium")
        countryLabelList.add("Bulgaria")
        countryLabelList.add("Brazil")
        countryLabelList.add("Canada")
        countryLabelList.add("Switzerland")
        countryLabelList.add("China")
        countryLabelList.add("Columbia")
        countryLabelList.add("Cuba")
        countryLabelList.add("Czechia")
        countryLabelList.add("Gremany")
        countryLabelList.add("Egypt")
        countryLabelList.add("France")
        countryLabelList.add("Great Britain")
        countryLabelList.add("Greece")
        countryLabelList.add("Hong Kong")
        countryLabelList.add("Hungary")
        countryLabelList.add("Indonesia")
        countryLabelList.add("Ireland")
        countryLabelList.add("Israel")
        countryLabelList.add("Italy")
        countryLabelList.add("Japan")
        countryLabelList.add("Korea")
        countryLabelList.add("Lithuania")
        countryLabelList.add("Latvia")
        countryLabelList.add("Morocco")
        countryLabelList.add("Mexico")
        countryLabelList.add("Malaysia")
        countryLabelList.add("Nigeria")
        countryLabelList.add("Netherlands")
        countryLabelList.add("Norway")
        countryLabelList.add("New Zealand")
        countryLabelList.add("Philippines")
        countryLabelList.add("Poland")
        countryLabelList.add("Portugal")
        countryLabelList.add("Romania")
        countryLabelList.add("Serbia")
        countryLabelList.add("Russia")
        countryLabelList.add("Saudi Arabia")
        countryLabelList.add("Sweden")
        countryLabelList.add("Singapore")
        countryLabelList.add("Slovenia")
        countryLabelList.add("Slovakia")
        countryLabelList.add("Thailand")
        countryLabelList.add("Turkey")
        countryLabelList.add("Taiwan")
        countryLabelList.add("Ukraine")
        countryLabelList.add("United States")
        countryLabelList.add("Venezuela")
        countryLabelList.add("South Africa")

        return countryLabelList
    }
}