import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Get the dimensions of the button
        val width = measuredWidth
        val height = measuredHeight

        // Calculate the size for the drawable (making it slightly smaller than the button)
        val drawableSize = (Math.min(width, height) * 0.8).toInt()

        // Set the new dimensions for the drawable
        setMeasuredDimension(drawableSize, drawableSize)
    }
}
