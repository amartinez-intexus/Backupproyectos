package com.example.pocantelop

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pocantelop.databinding.RequestIdBinding

class CustomDialog: DialogFragment() {

    private var _binding: RequestIdBinding? = null
    private val binding get() = _binding!!
    private lateinit var listener: CustomDialogListener

    interface CustomDialogListener {
        fun onDialogPositiveClick(dialog: Dialog?, id: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            _binding = RequestIdBinding.inflate(inflater)
            builder.setView(inflater.inflate(R.layout.request_id, null))
                .setPositiveButton( requireContext().getString(R.string.confirm_positive_button)) { _, _ ->
                    listener.onDialogPositiveClick(dialog, binding.userId.text.toString())
                }
            builder.setView(binding.root).create()
        } ?: throw IllegalStateException("Activity null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as CustomDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement CustomDialogListener"))
        }
    }

}