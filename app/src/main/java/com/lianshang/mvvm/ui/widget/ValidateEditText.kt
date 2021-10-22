package com.lianshang.mvvm.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ColorUtils
import com.lianshang.mvvm.R

class ValidateEditText : ConstraintLayout,View.OnClickListener{
    lateinit var etText:EditText
    lateinit var ivExt:ImageView
    lateinit var tvTip:TextView

    var inputType:Int=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    var isShowEye:Boolean=true
    var hintText:String=""
    var isShowTip:Boolean=true
    var editTipText:String=""
    var failColor:Int=ColorUtils.getColor(R.color.app_red)
    var successColor:Int=ColorUtils.getColor(R.color.app_green)
    var resultIcon:Int=0

    var isValidate:Boolean=false
    var maxLength:Int=20

    constructor(@NonNull context: Context):this(context, null){

    }

    constructor(
        @NonNull context: Context,
        @Nullable attrs: AttributeSet?,
    ):this(context, attrs, 0){
        View.inflate(context, R.layout.layout_validate_edit_text, this)
        etText=this.findViewById(R.id.et_text)
        ivExt=this.findViewById(R.id.iv_ext)
        ivExt.setOnClickListener(this)
        tvTip=this.findViewById(R.id.tv_edit_tip)
        attrs?.also {
            //获取属性
            val typedArray: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.ValidateEditText
            )
            inputType=typedArray.getInt(
                R.styleable.ValidateEditText_inputType,
                etText.inputType
            )
            isShowEye=typedArray.getBoolean(R.styleable.ValidateEditText_isShowEye, true)
            hintText= typedArray.getString(R.styleable.ValidateEditText_hintText).toString()
            isShowTip=typedArray.getBoolean(R.styleable.ValidateEditText_isShowTip, true)
            editTipText=typedArray.getString(R.styleable.ValidateEditText_editTipText).toString()
            failColor=typedArray.getColor(R.styleable.ValidateEditText_failColor, failColor)
            successColor=typedArray.getColor(
                R.styleable.ValidateEditText_successColor,
                successColor
            )
            resultIcon=typedArray.getResourceId(R.styleable.ValidateEditText_resultIcon,0)
            maxLength=typedArray.getInt(R.styleable.ValidateEditText_maxInputLength,20)
            typedArray.recycle()
        }
        if(inputType==16){
            etText.inputType=(InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_VARIATION_PASSWORD))
        }else {
            etText.inputType = inputType
        }
        etText.hint=hintText
        etText.filters= arrayOf(InputFilter.LengthFilter(maxLength))
        ivExt.visibility=if(isShowEye)View.VISIBLE else View.GONE

        tvTip.text=editTipText
        tvTip.visibility=if(isShowTip)View.VISIBLE else View.GONE
        if(resultIcon>0) tvTip.setCompoundDrawablesWithIntrinsicBounds(
            resultIcon,
            0,
            0,
            0
        )
    }

    constructor(
        @NonNull context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ):super(context, attrs, defStyleAttr){

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_ext -> {
                ivExt.isSelected = !ivExt.isSelected
                if (ivExt.isSelected) {
                    etText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    etText.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                etText.setSelection(etText.text.toString().length)
            }
        }
    }

    fun setValidateTip(isOk:Boolean,iconRes:Int=0,tip:String=""){
        isValidate=isOk
        if(tvTip.visibility==View.GONE) tvTip.visibility=View.VISIBLE
        tvTip.setTextColor(if(isOk)successColor else failColor)
        if(iconRes!=0){
            tvTip.setCompoundDrawablesWithIntrinsicBounds(
                iconRes,
                0,
                0,
                0
            )
        }
        if(!TextUtils.isEmpty(tip)){
            editTipText=tip
            tvTip.text=tip
        }
    }

}