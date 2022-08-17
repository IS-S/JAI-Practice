require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
patterns:
    $Card = (2)
    $App = (1)
theme: /

    state: Start
        q!: $regex</start>
        a: Начнём.
        
    state: Change_Code 
        intent!: /Смена кода
        script:
            if($parseTree._INSTANCE=="приложение"){
                $reactions.transition("/AppConsult");
            }
            else if($parseTree._INSTANCE=="карта"){
                
                $reactions.transition("/CardConsult");
            }
            else{
                $reactions.answer("Сейчас расскажу порядок действий. Выберите, что именно планируете сделать:\n1. Поменять пароль для входа в приложение.\n2. Поменять PIN-код от карты.\n\nПожалуйста, отправьте цифру, соответствующую вашему выбору.");
                $reactions.transition("/Choice");
            }
            
    state: Choice
        state: App
            q: * $App *
            go!:/AppConsult
        state: Card
            q: * $Card *
            go!:/CardConsult
            
            
    state: AppConsult
        script:
            $reactions.answer("Смена пароля от приложения возможна несколькими способами:\n1. На экране \"Профиль\" выберите \"Изменить код в приложение\".\n2. Введите SMS-код.\n3. Придумайте новый код для входа в приложение и повторите его.");
            $reactions.answer("Либо нажмите на кнопку \"Выйти\" на странице ввода пароля для входа в приложение.\n\nПосле чего нужно будет заново пройти регистрацию:\n1. ввести полный номер карты )если оформляли ранее, иначе номер телефона и дату рождения).\n2. указать код из смс-код.\n3. придумать новый пароль для входа.");
            $reactions.transition("/EndSession");
            
    state: CardConsult
        script:
            $reactions.answer("Это можно сделать в приложении.\n1. На экране \"Мои деньги\" в разделе \"Карты\" нажмите на нужную.\n2. Выберите вкладку \"Настройки\".\n3. Нажмите \"Сменить пин-код\".\n4. И введите комбинацию, удобную вам.\n5. Повторите её.");
            $reactions.answer("И все готово!\nПин-код установлен, можно пользоваться. &#128578");
            $reactions.transition("/EndSession");

    state: EndSession
        a: Приятно было пообщаться. Всегда готов помочь вам снова &#128578


    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}