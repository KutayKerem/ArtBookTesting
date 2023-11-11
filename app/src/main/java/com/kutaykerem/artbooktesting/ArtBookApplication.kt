package com.kutaykerem.artbooktesting

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// SÜREKLİ veri çekmemesi için hilt ile bi kere çekip bidaha çekmemye yarıyor bu hilt
@HiltAndroidApp
class ArtBookApplication: Application()