package model

import "time"

type BloodPressureReading struct {
	Systolic  int       `json:"systolic"`
	Diastolic int       `json:"diastolic"`
	Pulse     int       `json:"pulse"`
	TakenAt   time.Time `json:"takenAt"`
}
