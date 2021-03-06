package io.github.iurimenin.easyprod.app.cultivation.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.iurimenin.easyprod.R
import io.github.iurimenin.easyprod.app.cultivation.model.CultivationModel
import io.github.iurimenin.easyprod.app.util.CallbackInterface
import kotlinx.android.synthetic.main.item_cultivation.view.*

/**
 * Created by Iuri Menin on 01/06/17.
 */
class CultivationAdapter(val mCallback : CallbackInterface,
                         val mCultivations: ArrayList<CultivationModel>,
                         val clickListener: (CultivationModel) -> Unit) :
        RecyclerView.Adapter<CultivationAdapter.ViewHolder>() {

    val selectedItems = ArrayList<CultivationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CultivationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cultivation, parent, false)
        return CultivationAdapter.ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: CultivationAdapter.ViewHolder, position: Int) {
        holder.bindCultivation(mCultivations[position], selectedItems, holder.itemView.context, mCallback)
    }

    override fun getItemCount() = mCultivations.size

    class ViewHolder(view: View,
                     val clickListener: (CultivationModel) -> Unit) :
            RecyclerView.ViewHolder(view) {

        fun bindCultivation(field: CultivationModel, selectedItems: ArrayList<CultivationModel>, context: Context,
                            callback: CallbackInterface) {
            with(field) {
                itemView.textViewCultivation.text = name
                itemView.textViewSeason.text = seasonName
                itemView.constraintLayoutItemCultivation.setOnClickListener {
                    if (selectedItems.size > 0)
                        selectDeselectItem(itemView, field, selectedItems, context, callback)
                    else
                        clickListener(this)
                }
                itemView.constraintLayoutItemCultivation.setOnLongClickListener {
                    selectDeselectItem(itemView, field, selectedItems, context, callback); true
                }
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIcons))
            }
        }

        private fun selectDeselectItem(itemView: View?, field: CultivationModel,
                                       selectedItems: ArrayList<CultivationModel>, context: Context,
                                       callback : CallbackInterface) {
            if(selectedItems.contains(field)){
                selectedItems.remove(field)
                itemView?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIcons))
            } else {
                selectedItems.add(field)
                itemView?.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivider))
            }

            callback.updateMenuIcons(selectedItems.size)
        }
    }

    fun addItem(field : CultivationModel) {
        mCultivations.add(field)
        this.notifyDataSetChanged()
    }

    fun  updateItem(updated: CultivationModel) {

        mCultivations
                .filter { it.key == updated.key }
                .forEach {
                    it.name = updated.name
                    it.seasonKey = updated.seasonKey
                    it.seasonName = updated.seasonName
                }
        this.notifyDataSetChanged()
    }

    fun removeItem(field: CultivationModel) {
        mCultivations.remove(field)
        selectedItems.remove(field)
        this.notifyDataSetChanged()
    }

    fun removeSelection() {
        selectedItems.clear()
        mCallback.updateMenuIcons(itemCount)
        this.notifyDataSetChanged()
    }
}