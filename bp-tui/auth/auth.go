package auth

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"
)

type LoginRequest struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

type LoginResponse struct {
	Token string `json:"token"`
}

const tokenFile = ".bp-token"

func Login(apiURL, username, password string) (string, error) {
	reqBody, err := json.Marshal(LoginRequest{Username: username, Password: password})
	if err != nil {
		return "", err
	}

	resp, err := http.Post(apiURL+"/auth/login", "application/json", bytes.NewBuffer(reqBody))
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return "", fmt.Errorf("login failed: status %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	var loginResp LoginResponse
	if err := json.Unmarshal(body, &loginResp); err != nil {
		return "", err
	}

	if loginResp.Token == "" {
		return "", errors.New("empty token returned")
	}

	// Save token locally
	err = saveToken(loginResp.Token)
	if err != nil {
		return "", fmt.Errorf("token received, but could not save: %w", err)
	}

	return loginResp.Token, nil
}

func saveToken(token string) error {
	home, err := os.UserHomeDir()
	if err != nil {
		return err
	}

	path := filepath.Join(home, tokenFile)
	return os.WriteFile(path, []byte(token), 0600)
}

func LoadToken() (string, error) {
	home, err := os.UserHomeDir()
	if err != nil {
		return "", err
	}

	path := filepath.Join(home, tokenFile)
	data, err := os.ReadFile(path)
	if err != nil {
		return "", err
	}

	return string(data), nil
}
