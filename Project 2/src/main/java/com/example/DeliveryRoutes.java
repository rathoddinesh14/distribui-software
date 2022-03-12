package com.example;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.example.UserRegistry.User;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Routes can be defined in separated classes like shown in here
 */
// #user-routes-class
public class DeliveryRoutes {
	// #user-routes-class
	private final static Logger log = LoggerFactory.getLogger(DeliveryRoutes.class);
	// private final ActorRef<UserRegistry.Command> userRegistryActor;
	private final ActorRef<Delivery.Command> deliveryActor;
	private final Duration askTimeout;
	private final Scheduler scheduler;

	public DeliveryRoutes(ActorSystem<?> system, ActorRef<Delivery.Command> deliveryActor) {
		// this.userRegistryActor = userRegistryActor;
		this.deliveryActor = deliveryActor;
		scheduler = system.scheduler();
		askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
	}

	// private CompletionStage<UserRegistry.GetUserResponse> getUser(String name) {
	// return AskPattern.ask(userRegistryActor, ref -> new
	// UserRegistry.GetUser(name, ref), askTimeout, scheduler);
	// }

	// private CompletionStage<UserRegistry.ActionPerformed> deleteUser(String name)
	// {
	// return AskPattern.ask(userRegistryActor, ref -> new
	// UserRegistry.DeleteUser(name, ref), askTimeout, scheduler);
	// }

	// private CompletionStage<UserRegistry.Users> getUsers() {
	// return AskPattern.ask(userRegistryActor, UserRegistry.GetUsers::new,
	// askTimeout, scheduler);
	// }

	// private CompletionStage<UserRegistry.ActionPerformed> createUser(User user) {
	// return AskPattern.ask(userRegistryActor, ref -> new
	// UserRegistry.CreateUser(user, ref), askTimeout, scheduler);
	// }

	// reInitialize the delivery service
	private CompletionStage<Delivery.ReInitializeResponse> reInitialize() {
		log.info("serving reInitialize request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	private CompletionStage<Delivery.ReInitializeResponse> agentSignIn() {
		log.info("serving agentSignIn request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	private CompletionStage<Delivery.ReInitializeResponse> requestOrder() {
		log.info("serving requestOrder request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	private CompletionStage<Delivery.ReInitializeResponse> agentSignOut() {
		log.info("serving agentSignOut request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	private CompletionStage<Delivery.ReInitializeResponse> orderDelivered() {
		log.info("serving orderDelivered request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	private CompletionStage<Delivery.ReInitializeResponse> getOrder() {
		log.info("serving getOrder request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	private CompletionStage<Delivery.ReInitializeResponse> getAgent() {
		log.info("serving getAgent request");
		return AskPattern.ask(deliveryActor, Delivery.ReInitialize::new, askTimeout, scheduler);
	}

	/**
	 * This method creates one route (of possibly many more that will be part of
	 * your Web App)
	 */
	// #all-routes
	// public Route userRoutes() {
	// return pathPrefix("users", () -> concat(
	// // #users-get-delete
	// pathEnd(() -> concat(
	// get(() -> onSuccess(getUsers(),
	// users -> complete(StatusCodes.OK, users, Jackson.marshaller()))),
	// post(() -> entity(
	// Jackson.unmarshaller(User.class),
	// user -> onSuccess(createUser(user), performed -> {
	// log.info("Create result: {}", performed.description);
	// return complete(StatusCodes.CREATED, performed, Jackson.marshaller());
	// }))))),
	// // #users-get-delete
	// // #users-get-post
	// path(PathMatchers.segment(), (String name) -> concat(
	// get(() ->
	// // #retrieve-user-info
	// rejectEmptyResponse(() -> onSuccess(getUser(name), performed ->
	// complete(StatusCodes.OK,
	// performed.maybeUser.isPresent() ? performed.maybeUser.get() : "NONE",
	// Jackson.marshaller())))
	// // #retrieve-user-info
	// ),
	// delete(() ->
	// // #users-delete-logic
	// onSuccess(deleteUser(name), performed -> {
	// log.info("Delete result: {}", performed.description);
	// return complete(StatusCodes.OK, performed, Jackson.marshaller());
	// })
	// // #users-delete-logic
	// )))
	// // #users-get-post
	// ));
	// }
	// #all-routes

	/**
	 * request - post /reInitialize
	 * response - http code 201
	 */
	Route reInitializeRoute = concat(
			pathEnd(() -> concat(
					post(() -> onSuccess(reInitialize(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * request - post /agentSignIn
	 * requestbody - {"agentId": num}
	 * response - http code 201
	 */
	Route agentSignInRoute = concat(
			pathEnd(() -> concat(
					post(() -> onSuccess(agentSignIn(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * request      - get /agent/num
	 * response     - http code 200
	 * 				- {"agentId": num, "status": y}
	 */
	Route agentRoute = concat(
			pathEnd(() -> concat(
					get(() -> onSuccess(getAgent(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * request      - get /order/num
	 * response     - http code 404 if no order
	 * 				- http code 200 if has order
	 * 				- {"orderId":num, "status": x, "agentId": y}
	 */
	Route orderRoute = concat(
			pathEnd(() -> concat(
					get(() -> onSuccess(getOrder(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * request - post /orderDelivered
	 * requestbody - {"orderId": num}
	 * response - http code 201
	 */
	Route orderDeliveredRoute = concat(
			pathEnd(() -> concat(
					post(() -> onSuccess(orderDelivered(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * request - post /agentSignOut
	 * requestbody - {"agentId": num}
	 * response - http code 201
	 */
	Route agentSignOutRoute = concat(
			pathEnd(() -> concat(
					post(() -> onSuccess(agentSignOut(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * request      - post /requestOrder
	 * requestbody  - {"custId": num, "restId": x, "itemId": y, "qty": z}
	 * response     - http code 201 if success { "orderId": num }
	 * 				- http code 410 if fail
	 */
	Route requestOrderRoute = concat(
			pathEnd(() -> concat(
					post(() -> onSuccess(requestOrder(), (t) -> complete(StatusCodes.OK))))));

	/**
	 * Route : /reInitialize post
	 */
	public Route deliveryRoutes() {
		return concat(
				pathPrefix("reInitialize", () -> reInitializeRoute),
				pathPrefix("agentSignIn", () -> agentSignInRoute),
				pathPrefix("requestOrder", () -> requestOrderRoute),
				pathPrefix("agentSignOut", () -> agentSignOutRoute),
				pathPrefix("orderDelivered", () -> orderDeliveredRoute),
				pathPrefix("order", () -> orderRoute),
				pathPrefix("agent", () -> agentRoute));
	}
}