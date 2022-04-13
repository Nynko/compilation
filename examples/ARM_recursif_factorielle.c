int factorielle(int a){
	if(a<=0){
		return 1;
	}

	else{
		return factorielle(a-1)* a ;				        
	}

}

int main(){
	int a;
	print(factorielle(4));
	return 0;

}
