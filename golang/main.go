package main

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/go-redis/redis/v8"
	"github.com/gofiber/fiber/v2"
)

const serverPort int = 5001

type User struct {
	Name  string `json:"name"`
	Email string `json:"email"`
}

var ctx = context.Background()

var redisClient = redis.NewClient(&redis.Options{
	Addr: "localhost:6379",
})

func main() {
	app := fiber.New()

	app.Post("/", func(c *fiber.Ctx) error {
		user := new(User)

		if err := c.BodyParser(user); err != nil {
			panic(err)
		}

		payload, err := json.Marshal(user)
		if err != nil {
			panic(err)
		}

		if err := redisClient.Publish(ctx, "send-user-data", payload).Err(); err != nil {
			panic(err)
		}

		return c.SendStatus(200)
	})
	go subscriber()
	fmt.Println("starting server on port", serverPort)
	app.Listen(fmt.Sprintf(":%d", serverPort))
}

func subscriber() {
	fmt.Println("startin subscriber...")
	subscriber := redisClient.Subscribe(ctx, "send-user-data")

	user := User{}

	for {
		msg, err := subscriber.ReceiveMessage(ctx)
		if err != nil {
			panic(err)
		}

		if err := json.Unmarshal([]byte(msg.Payload), &user); err != nil {
			panic(err)
		}

		fmt.Println("Received message from " + msg.Channel + " channel.")
		fmt.Printf("%+v\n", user)
	}
}
