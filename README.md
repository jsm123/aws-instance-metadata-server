To start the service and test it:
- install maven
- run maven package
- run "java -jar target/aws-instance-metadata-server-1.0-SNAPSHOT.jar -DaccessKey=YOUR_ACCESS_KEY -DsecretKey=YOUR_SECRET_KEY"
- open http://your.fqdn:8080/latest/meta-data/iam/security-credentials
-- it will show you your allowed role names
- open http://your.fqdn:8080/latest/meta-data/iam/security-credentials/$yourRoleName$
-- it will assume this $yourRoleName$ and show you credentials for it