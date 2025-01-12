package com.example.car_maintenance_project;

import model.Ticket;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewTicketsAdapter extends RecyclerView.Adapter<ViewTicketsAdapter.ViewHolder> {

    private final List<Ticket> tickets;

    public ViewTicketsAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_tickets_card, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        CardView cardView = holder.cardView;

        // Set ticket ID
        TextView ticketId = cardView.findViewById(R.id.ticket_id);
        ticketId.setText("Ticket ID: " + ticket.getTicketId());

        // Set service provided
        TextView serviceProvided = cardView.findViewById(R.id.service_provided);
        serviceProvided.setText("Service: " + ticket.getServiceName());

        // Set submitted date
        TextView submittedDate = cardView.findViewById(R.id.submitted_date);
        submittedDate.setText("Submitted Date: " + ticket.getSubmittedDate());

        // Set customer name
        TextView customerName = cardView.findViewById(R.id.customer_name);
        customerName.setText("Customer: " + ticket.getCustomerName());

        // Set emergency level
        TextView emergencyLevel = cardView.findViewById(R.id.emergency_level);
        emergencyLevel.setText("Emergency: " + ticket.getEmergencyLevel());

        // Set status
        TextView status = cardView.findViewById(R.id.status);
        status.setText("Status: " + ticket.getStatus());
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