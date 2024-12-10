package com.hasanbasyildiz.learnconnect.wishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hasanbasyildiz.learnconnect.R
import com.hasanbasyildiz.learnconnect.databinding.FragmentProfilBinding
import com.hasanbasyildiz.learnconnect.databinding.FragmentWishlistBinding


class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    private lateinit var viewModel: WishlistViewModel
    private lateinit var adapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WishlistViewModel::class.java]
        adapter = WishlistAdapter()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.wishlist.observe(viewLifecycleOwner) { wishlist ->
            if (wishlist.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.updateWishlist(wishlist)
            }
        }

        viewModel.loadUserDataAndFetchWishlist()
    }
}
