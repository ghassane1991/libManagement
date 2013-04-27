package reservationManagement;

import javax.persistence.PreRemove;

import exceptions.ReserveBookException;


public class ReservationListener {
	@PreRemove
	public void onReservationPreRemove(Reservation r){
		if (r.getBook() != null && r.getSubscriber()!=null){
			try {
				r.cancel();
			} catch (ReserveBookException e) {

			}
		}
	}
}
