// @/sub/main.go
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"strings"
	"time"

	"github.com/go-redis/redis/v8"
)

const (
	dateTimePattern string = "2006-01-02T15:04:05.999999"
	redisAddress    string = "localhost:6379"
	aebChannel      string = "STATUS_CHANNEL"
)

type LocalDateTime time.Time

func (c *LocalDateTime) UnmarshalJSON(b []byte) error {
	value := strings.Trim(string(b), `"`) //get rid of "
	if value == "" || value == "null" {
		return nil
	}
	t, err := time.Parse(dateTimePattern, value) //parse time
	if err != nil {
		return err
	}
	*c = LocalDateTime(t) //set result using the pointer
	return nil
}

func (c LocalDateTime) MarshalJSON() ([]byte, error) {
	return []byte(`"` + time.Time(c).Format(dateTimePattern) + `"`), nil
}

func (c LocalDateTime) String() string {
	return string(time.Time(c).Format(dateTimePattern))
}

type Processo struct {
	Status     string        `json:"status"`
	LastUpdate LocalDateTime `json:"lastUpdate"`
}

var ctx = context.Background()

var redisClient = redis.NewClient(&redis.Options{
	Addr: redisAddress,
})

func main() {
	subscriber := redisClient.Subscribe(ctx, aebChannel)

	processo := Processo{}

	for {
		msg, err := subscriber.ReceiveMessage(ctx)
		if err != nil {
			panic(err)
		}

		if err := json.Unmarshal([]byte(msg.Payload), &processo); err != nil {
			panic(err)
		}

		fmt.Println("Received message from " + msg.Channel + " channel.")
		fmt.Println("payload " + msg.Payload)
		fmt.Printf("%+v\n", processo)
		// teste := time.Time(processo.LastUpdate)
		// fmt.Printf("%+v\n", teste.Format("2006-01-02T15:04:05.999999"))
	}
}
