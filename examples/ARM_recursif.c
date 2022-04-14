int func(int a){
	if(a<=0){
		return 0;
	}

	else{
		return func(a-1) + 1 ;				        }

}

int main(){
	int a;
	a = func(1001);
	print(a);
	return 0;

}
