#!/bin/bash

# Do not run this script
exit

if vault kv list secrets/ &>/dev/null; then
    TOKEN="$(vault kv get --field=value secrets/java/youtubemp3downloader_basic_auth)"
    [[ "$?" -eq 0 ]] && echo "Token acquired" || echo "Token retrieval failed"
else
    echo "Not logged into vault."
fi

curl --location --silent --request "GET" \
    --url "$URL/api/v1/download/hn5ZiARBLEY/<recaptcha-response>" \
    --header "Authorization: Basic $TOKEN"

