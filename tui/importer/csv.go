package importer

import (
	"encoding/csv"
	"fmt"
	"io"
	"os"
	"strconv"
	"time"

	"github.com/shurikai/systolic-tui/model"
)

func ParseCSV(path string) ([]model.BloodPressureReading, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, fmt.Errorf("unable to open file: %w", err)
	}
	defer func(file *os.File) {
		err := file.Close()
		if err != nil {
			
		}
	}(file)

	reader := csv.NewReader(file)
	reader.TrimLeadingSpace = true

	// Read header
	headers, err := reader.Read()
	if err != nil {
		return nil, fmt.Errorf("unable to read header: %w", err)
	}

	if len(headers) < 4 {
		return nil, fmt.Errorf("expected at least 4 columns, got %d", len(headers))
	}

	var readings []model.BloodPressureReading

	for {
		row, err := reader.Read()
		if err == io.EOF {
			break
		} else if err != nil {
			return nil, fmt.Errorf("error reading CSV: %w", err)
		}

		systolic, err := strconv.Atoi(row[0])
		if err != nil {
			return nil, fmt.Errorf("invalid systolic value: %w", err)
		}

		diastolic, err := strconv.Atoi(row[1])
		if err != nil {
			return nil, fmt.Errorf("invalid diastolic value: %w", err)
		}

		pulse, err := strconv.Atoi(row[2])
		if err != nil {
			return nil, fmt.Errorf("invalid pulse value: %w", err)
		}

		takenAt, err := time.Parse(time.RFC3339, row[3])
		if err != nil {
			return nil, fmt.Errorf("invalid timestamp: %w", err)
		}

		readings = append(readings, model.BloodPressureReading{
			Systolic:  systolic,
			Diastolic: diastolic,
			Pulse:     pulse,
			TakenAt:   takenAt,
		})
	}

	return readings, nil
}
