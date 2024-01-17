package ir.abolfazl.abolmovie.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentIntroBinding
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity

class FragmentIntro : Fragment() {
    lateinit var binding: FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.INVISIBLE
        binding.btnGetIn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentIntro2_to_fragmentLogin)
        }

    }

}