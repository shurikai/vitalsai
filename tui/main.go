package main

import (
	"fmt"
	"os"
	"time"

	"github.com/shurikai/systolic-tui/importer"
)

func main() {
	fmt.Println("Welcome to the BP Tracker TUI")
	fmt.Println("-----------------------------")
	fmt.Println("1. Login")
	fmt.Println("2. Import Readings from CSV")
	fmt.Println("3. Import Readings from JSON")
	fmt.Println("4. Submit Readings to Backend")
	fmt.Println("5. Exit")

	var choice int
	fmt.Print("Choose an option: ")
	_, err := fmt.Scanf("%d\n", &choice)
	if err != nil {
		fmt.Println("Invalid input:", err)
		os.Exit(1)
	}

	switch choice {
	case 1:
		fmt.Println("Login not implemented yet")
	case 2:
		fmt.Print("Enter CSV file path: ")
		var path string
		_, err := fmt.Scanln(&path)
		if err != nil {
			return
		}

		readings, err := importer.ParseCSV(path)
		if err != nil {
			fmt.Println("Error:", err)
		} else {
			fmt.Printf("Imported %d readings:\n", len(readings))
			for _, r := range readings {
				fmt.Printf("  %d/%d @ %d bpm on %s\n", r.Systolic, r.Diastolic, r.Pulse, r.TakenAt.Format(time.RFC822))
			}
		}
	case 3:
		fmt.Println("JSON Import not implemented yet")
	case 4:
		fmt.Println("Submission not implemented yet")
	case 5:
		fmt.Println("Goodbye!")
		os.Exit(0)
	default:
		fmt.Println("Invalid choice")
	}
}
