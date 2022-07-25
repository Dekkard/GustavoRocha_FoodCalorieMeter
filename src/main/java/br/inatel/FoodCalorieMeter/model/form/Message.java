package br.inatel.FoodCalorieMeter.model.form;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

/**
 * Classe modelo utilizado para representar mensagens ao front-end de forma
 * facilitada e reduzida. Representa também a resposta do servidor a uma
 * requisição.
 * 
 * @param <Entity>
 */
@Getter
public class Message<E> extends ResponseEntity<E>{
	private MessageBody body;

	@Getter
	public static class MessageBody{
		private LocalDateTime timestamp;
		private Integer status;
		private String reason;
		private String message;
		
		public MessageBody(HttpStatus status, String message) {
			this.timestamp = LocalDateTime.now();
			this.status = status.value();
			this.reason = status.getReasonPhrase();
			this.message = message;
		}
	}

	public Message(HttpStatus status, String message) {
		super(status);
		this.body = new MessageBody(status, message);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E getBody() {
		return (E) this.body;
	}
	
}
