1.客户端自动登录证书生成
	ssh-keygen -m PEM -t rsa
2.将PUBLIC证书内容放到服务端authorized_keys中