package net.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_progress_dialog.*
import net.basicmodel.R

class ProgressDialogFragment : DialogFragment() {
    fun getInstance(): ProgressDialogFragment {
        val fragment = ProgressDialogFragment()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress.indeterminateDrawable.setColorFilter(
            requireContext().resources.getColor(R.color.colorAccent),
            PorterDuff.Mode.SRC_IN
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { dialog, keyCode, event -> // Disable Back key and Search key
            when (keyCode) {
                KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_SEARCH -> true
                else -> false
            }
        }
        return dialog

    }
}
