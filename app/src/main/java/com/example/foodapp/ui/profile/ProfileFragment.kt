package com.example.foodapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodapp.auth.AuthRepository
import com.example.foodapp.auth.LoginActivity
import com.example.foodapp.crash.CrashLogger
import com.example.foodapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val authRepository = AuthRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        binding.tvEmail.text = user?.email ?: "Usuario sin correo"
        binding.tvUid.text = "Sesión activa: ${user?.uid?.take(8) ?: "sin UID"}"
        binding.tvProfileInfo.text = "Desde este perfil puedes revisar tu cuenta, acceder al historial de pedidos, probar Crashlytics y cerrar sesión de forma segura."
        binding.btnTestCrash.setOnClickListener {
            CrashLogger().logNonFatalError(
                "ProfileFragment.btnTestCrash",
                IllegalStateException("Falla de prueba controlada generada desde el perfil de FoodApp")
            )
            Toast.makeText(
                requireContext(),
                "Falla de prueba registrada. Revisa Crashlytics en Firebase.",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btnLogout.setOnClickListener {
            authRepository.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
