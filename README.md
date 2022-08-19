# Evaluation Project II
Projeto de escopo aberto para a avaliação técnica 2
## FCM - Food Calorie Meter
Projeto que se empenha em realizar cálculo de nutrientes baseados em uma lista de alimentos e suas quantidades.

### Execução
Para realizar a execução do projeto, basta preencher os dados do compose-default.yml, com as chaves e urls necessárias para a execução do projeto. Dependente apenas do container MySQL.
	
	- JWT_CKEY: Chave secreta do JWT
	- FDC_KEY: Chave do FDC
	- FDC_URL: URL do FDC
	- MM_KEY: Chave para o MyMemory
	- MM_HOST: Host do MyMemory
	- DB_USER: Usuário do DB
	- DB_PASS: Senha para o DB
	- DB_HOST: Domínio do DB
	- HOST: Domínio da API
	- PORT: Porta da API