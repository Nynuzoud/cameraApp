package com.example.sergeykuchin.adorablecameraapp.view.imagesetup

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.FragmentImageSetupBinding
import com.example.sergeykuchin.adorablecameraapp.other.databinding.FragmentDataBindingComponent
import com.example.sergeykuchin.adorablecameraapp.other.extensions.simpleSubscribe
import com.example.sergeykuchin.adorablecameraapp.other.utils.Utils
import com.example.sergeykuchin.adorablecameraapp.view.CommonFragment
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterAdapter
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterAdapterListener
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterGenerator
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.ImageFilter
import com.example.sergeykuchin.adorablecameraapp.view.main.MainActivity
import com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup.ImageSetupFragmentVM
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ImageSetupFragment : CommonFragment<ImageSetupFragmentVM, FragmentImageSetupBinding>(), ImageSetupFragmentView {

    companion object {

        const val FILE_URI = "file_uri"
    }

    @Inject
    lateinit var utils: Utils

    private lateinit var presenter: ImageSetupPresenter

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private val filterGenerator = FilterGenerator()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_setup, container, false, dataBindingComponent)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageSetupFragmentVM::class.java)

        (activity as MainActivity).enableActionBar(binding.appBar.toolbar)
        setHasOptionsMenu(true)

        presenter = ImageSetupPresenterImpl(this, utils, filterGenerator, context!!.contentResolver, viewModel)

        disposable.add(viewModel.loadingVisibility.simpleSubscribe(
                onNext = {
                    binding.loading.visibility = it
                }))

        viewModel.setFileUri(arguments?.getString(FILE_URI))


        initFilterRecycler()

        presenter.setImageToImageView()

        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity?.menuInflater?.inflate(R.menu.menu_image_setup, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_share -> shareImage()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun shareImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/jpeg"

        val uri: Uri?
        val id = viewModel.selectedImageId
        uri = if (id == null) {
            viewModel.getFileUri()
        } else {
            viewModel.cachedBitmapUrisMap[viewModel.selectedImageId]
        }

        val photoUri = FileProvider.getUriForFile(
                context!!,
                getString(R.string.file_provider_authority),
                File(uri?.path))

        intent.putExtra(Intent.EXTRA_STREAM, photoUri)
        startActivity(Intent.createChooser(intent, "Share an image to.."))
    }

    override fun addCachedBitmapUri(id: Int, fileUri: Uri?) {
        Timber.d("addCachedBitmapUri")

        if (fileUri == null) return
        viewModel.cachedBitmapUrisMap[id] = fileUri
    }

    override fun setMainImageBitmap(bitmap: Bitmap?) {
        Timber.d("setMainImageBitmap")
        binding.image.setImageBitmap(bitmap)
    }

    private fun initFilterRecycler() {
        viewModel.filterRecyclerConfiguration.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filterRecyclerConfiguration.adapter = FilterAdapter()
        viewModel.filterRecyclerConfiguration.adapter?.setHasStableIds(true)
        (viewModel.filterRecyclerConfiguration.adapter as FilterAdapter).listener = FilterListener()
    }

    override fun generateFilters() {
        if (viewModel.filterList == null) {
            viewModel.filterList = filterGenerator.generateFilters(viewModel.getMinifiedBitmap())
        }
        (viewModel.filterRecyclerConfiguration.adapter as FilterAdapter).data = viewModel.filterList
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private inner class FilterListener : FilterAdapterListener {

        override fun onClick(imageFilter: ImageFilter) {
            Timber.d("onClick")
            viewModel.loadingVisibility.onNext(View.VISIBLE)
            viewModel.selectedImageId = imageFilter.id
            presenter.onFilterClick(imageFilter)
        }
    }
}