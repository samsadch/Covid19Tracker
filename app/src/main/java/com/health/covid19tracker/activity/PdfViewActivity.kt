package com.health.covid19tracker.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.health.covid19tracker.R


class PdfViewActivity : AppCompatActivity() {

    lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
        val stringBuilder = StringBuilder()
        //getting the asset pdf file and assign it into the odf viewer
        stringBuilder.append("pdf/clinical_management.pdf")
        this.pdfView = findViewById(R.id.pdfview);
        this.pdfView.fromAsset(stringBuilder.toString()).defaultPage(0).load()
    }
}
