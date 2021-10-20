package com.yurnero.mqtt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yurnero.mqtt.databinding.FragmentFirstBinding
import com.yurnero.mqtt.exchange.MessageManager
import com.yurnero.mqtt.ui.adapter.MessageAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            MessageManager.getInstance().publishMessage(binding.msg.text.toString())
            binding.msg.setText("")
        }
        val adapter = MessageAdapter(arrayListOf())
        binding.recycleView.adapter = adapter
        MessageManager.getInstance().getNewMessage()
            .observe(viewLifecycleOwner, { adapter.newMessage(it) })
        MessageManager.getInstance().init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}