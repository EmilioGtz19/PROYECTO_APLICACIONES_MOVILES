package com.example.masterfood.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.masterfood.ui.view.EditPassActivity
import com.example.masterfood.ui.view.EditProfileActivity
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.view.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnEditPass =  view.findViewById<Button>(R.id.btnEditPassword)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val txtUserNameProfile = view.findViewById<TextView>(R.id.txtUserNameProfile)
        val txtLastnameProfile = view.findViewById<TextView>(R.id.txtLastnameProfile)
        val txtEmailProfile = view.findViewById<TextView>(R.id.txtEmailProfile)
        val img = view.findViewById<ImageView>(R.id.imageProfile)

        this.loadPreference(txtUserNameProfile,txtLastnameProfile,txtEmailProfile,img)

        btnEdit.setOnClickListener{
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            requireActivity().startActivity(intent)
        }

        btnEditPass.setOnClickListener{
            val intent = Intent(requireActivity(), EditPassActivity::class.java)
            requireActivity().startActivity(intent)
        }

        btnLogout.setOnClickListener {
            UserApplication.prefs.logout()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().startActivity(intent)
        }


        // Inflate the layout for this fragment
        return view
    }

    private fun loadPreference(txtUserNameProfile : TextView, txtLastnameProfile : TextView, txtEmailProfile : TextView, img : ImageView ){
        val credential: UserModel = UserApplication.prefs.getUser()
        if (credential.first_name != "No information") {
            txtUserNameProfile.text = credential.first_name
            txtLastnameProfile.text = credential.last_name
            txtEmailProfile.text = credential.email
            img.setImageBitmap(ImageUtilities.getBitMapFromByteArray(credential.user_photo))
        }
    }

}