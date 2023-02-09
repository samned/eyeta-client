
package com.eyeta.client

import android.content.Context
import com.eyeta.client.PositionProvider.PositionListener

object PositionProviderFactory {

    fun create(context: Context, listener: PositionListener): PositionProvider {
        return AndroidPositionProvider(context, listener)
    }
}
