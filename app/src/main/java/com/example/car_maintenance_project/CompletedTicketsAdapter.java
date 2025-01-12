package com.example.car_maintenance_project;
import model.Ticket;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedTicketsAdapter extends RecyclerView.Adapter<CompletedTicketsAdapter.ViewHolder> {

    private final List<Ticket> tickets;

    public CompletedTicketsAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        CardView cardView = holder.cardView;

        // Set service name
        TextView serviceName = cardView.findViewById(R.id.service_name);
        serviceName.setText("Service Provided: " +ticket.getServiceName());

        // Set employee name
        TextView employeeName = cardView.findViewById(R.id.service_employee);
        employeeName.setText("Employee Name: " +ticket.getEmployeeName());

        // Set completion date
        TextView completionDate = cardView.findViewById(R.id.service_date);
        completionDate.setText("Completion Date: " +ticket.getCompletedDate());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}